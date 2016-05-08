package kr.blogspot.crowjdh.inspirationgen.music.models

import java.util.*

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 4.
 *
 * Bar
 */

class Bar(timeSignature: TimeSignature? = null): TickType {
    var timeSignature: TimeSignature
        get() {
            return _timeSignature ?: TimeSignature.default
        }
        set(value) {
            if (value.canContainTickType(this)) {
                _timeSignature = value
            }
        }
    val notables: ArrayList<Notable>
        get() = _notables

    val ticksLeft: Long
        get() = this.timeSignature.capableTicks() - ticks

    private var _notables: ArrayList<Notable> = arrayListOf()
    private var _timeSignature: TimeSignature? = null

    init {
        if (timeSignature != null) {
            this.timeSignature = timeSignature
        }
    }

    fun removeNotableAt(index: Int) = notables.removeAt(index)

    fun addNotableIgnoringResult(notable: Notable) {
        addNotableAndGetResult(notable)
    }

    fun addNotableAndGetResult(notable: Notable): Boolean {
        if (canAddNotable(notable)) {
            notables.add(notable)
            return true
        } else {
            return false
        }
    }

    override val ticks: Long
        get() = notables.map { it.ticks }.fold(0L) { prev, cur -> prev + cur }

    private fun canAddNotable(notable: Notable): Boolean {
        val targetTicks = ticks + notable.length.ticks()

        return targetTicks <= timeSignature.capableTicks()
    }

    companion object Generator {

        fun generate(builder: Options.() -> Unit) = generate(Options.create(builder))

        // TODO: Use length option to generate more than one bar
        fun generate(options: Options): Bar {
            val bar = Bar(options.timeSignature)
            bar.fillWithGeneratedNotables(options)
            bar.fillEmptyTicksWithRests()

            return bar
        }

        private fun Bar.fillWithGeneratedNotables(options: Options) {
            var trialCount = 0
            do {
                val notable = generateNotable(options)
                val addNotableSuccessful = this.addNotableAndGetResult(notable)
            } while(addNotableSuccessful && ++trialCount <= 50)
        }

        private fun Bar.fillEmptyTicksWithRests() {
            val noteLengths = NoteLength.fromTicks(this.ticksLeft)
            if (noteLengths != null) {
                for (noteLength in noteLengths) {
                    this.addNotableIgnoringResult(Rest(noteLength))
                }
            }
        }

        // TODO: Increase randomness
        private fun generateNotable(options: Options): Notable {
            val notableIdx = options.randomIntBelow(2)
            val noteLength = options.noteLengthRange.elementAt(
                    options.randomIntBelow(options.noteLengthRange.size))
            val pitch = options.randomIntInRange(options.pitchRange)

            val notable = when (notableIdx) {
                0 -> Note(noteLength, pitch)
                else -> Rest(noteLength)
            }
            return notable
        }

        class Options(var timeSignature: TimeSignature = TimeSignature.default,
                      var pitchRange: IntRange = 60..72,
                      var noteLengthRange: NoteLengthRange = NoteLengthRange.createDefault(),
                      var length: Int = 1,
                      var fixedSeed: Long? = null) {

            val seed: Long
                get() = fixedSeed ?: System.currentTimeMillis()

            companion object Factory {
                fun create(build: Options.() -> Unit): Options {
                    val options = Options()
                    options.build()
                    return options
                }
            }

            fun randomIntBelow(n: Int)
                    = Random(seed).nextInt(n)

            fun randomIntInRange(range: IntRange)
                    = Random(seed).nextInt(range.count()) + range.start
        }

        data class NoteLengthRange
        private constructor(val items: List<NoteLength>): Iterable<NoteLength> {

            val size: Int
                get() = items.size

            override fun iterator() = items.iterator()

            companion object Factory {
                fun createDefault() = create(NoteLength.QUARTER, NoteLength.EIGHTH)
                fun create(vararg items: NoteLength) = NoteLengthRange(items.toList().distinct())
            }
        }
    }
}
