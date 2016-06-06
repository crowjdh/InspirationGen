package kr.blogspot.crowjdh.inspirationgen.music.models

import kr.blogspot.crowjdh.inspirationgen.extensions.hashCodeWith
import kr.blogspot.crowjdh.inspirationgen.extensions.pickFromMap
import kr.blogspot.crowjdh.inspirationgen.model.FilterArrayList
import java.util.*
import java.util.concurrent.atomic.AtomicLong

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 4.
 *
 * Bar
 */

class Bar(timeSignature: TimeSignature? = null): TickType, Record {
    override var _id: Long = Record.invalidId
    var timeSignature = timeSignature ?: TimeSignature.createDefault()
        set(value) {
            if (value.canContainTickType(this)) {
                field = value
            }
        }
    val filter: (Notable) -> Boolean = {
        val targetTicks = ticks + it.length.ticks()
        targetTicks <= this.timeSignature.capableTicks()
    }
    val notables = FilterArrayList(filter)
    var program = Program.default

    val ticksLeft: Long
        get() = this.timeSignature.capableTicks() - ticks

    override val ticks: Long
        get() = notables.map { it.ticks }.fold(0L) { prev, cur -> prev + cur }

    override val records: List<Record>?
        get() = null

    override fun hashCode(): Int {
        return hashCodeWith(_id, this.timeSignature, notables, program)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Bar) {
            return false
        }
        return other._id.equals(this._id)
                && other.timeSignature.equals(this.timeSignature)
                && other.notables.equals(this.notables)
                && other.program.equals(this.program)
    }

    companion object Generator {

        fun generate(builder: Options.() -> Unit) = generate(Options.create(builder))

        fun generate(options: Options): List<Bar> {
            val bars = mutableListOf<Bar>()
            for (i in 1..options.barCount) {
                val bar = Bar(options.timeSignature)
                bar.program = options.program
                bar.fillWithGeneratedNotables(options)
                bar.fillEmptyTicksWithRests()
                bars.add(bar)
            }

            return bars
        }

        private fun Bar.fillWithGeneratedNotables(options: Options) {
            var trialCount = 0
            do {
                val notable = generateNotable(options)
                val addNotableSuccessful = this.notables.add(notable)
            } while(addNotableSuccessful && ++trialCount <= 50)
        }

        private fun Bar.fillEmptyTicksWithRests() {
            val noteLengths = NoteLength.fromTicks(this.ticksLeft)
            if (noteLengths != null) {
                for (noteLength in noteLengths) {
                    this.notables.add(Rest(noteLength))
                }
            }
        }

        private fun generateNotable(options: Options): Notable {
            val noteLength = generateRandomNoteLength(options)

            return if (shouldGenerateNote(options)) {
                Note(noteLength, generateRandomPitch(options))
            } else {
                Rest(noteLength)
            }
        }

        private fun shouldGenerateNote(options: Options)
                = options.randomIntBelow(100) < (100f * options.noteOverRestBias).toInt()

        private fun generateRandomNoteLength(options: Options): NoteLength
                = Random(options.seed).pickFromMap(options.noteLengthRange.items)

        private fun generateRandomPitch(options: Options)
                = options.randomItemInList(options.scale.pitches)

        private fun Options.randomIntBelow(n: Int)
                = Random(seed).nextInt(n)

        private fun <T> Options.randomItemInList(list: List<T>)
                = list[Random(seed).nextInt(list.count())]

        class Options(var timeSignature: TimeSignature = TimeSignature.createDefault(),
                      var scale: Scale = Scale.default,
                      var noteLengthRange: NoteLengthRange = NoteLengthRange.createDefault(),
                      var barCount: Int = 1,
                      var noteOverRestBias: Float = .5f,
                      var program: Program = Program.default,
                      var atomicBaseSeed: AtomicLong? = null): Record {

            override var _id: Long = Record.invalidId
            override val records = null

            val seed: Long
                get() = atomicBaseSeed?.andIncrement ?: System.nanoTime()

            override fun hashCode(): Int {
                return hashCodeWith(_id, timeSignature, scale, noteLengthRange,
                        barCount, noteOverRestBias, program, atomicBaseSeed)
            }

            override fun equals(other: Any?): Boolean {
                if (other !is Options) {
                    return false
                }
                return other._id.equals(this._id)
                        && other.timeSignature.equals(this.timeSignature)
                        && other.scale.equals(this.scale)
                        && other.noteLengthRange.equals(this.noteLengthRange)
                        && other.barCount.equals(this.barCount)
                        && other.noteOverRestBias.equals(this.noteOverRestBias)
                        && other.program.equals(this.program)
                        && other.atomicBaseSeed?.get() == this.atomicBaseSeed?.get()
            }

            fun validateOrThrow() {
                assert(barCount > 0) { "options.barCount MUST BE > 0" }
                assert(noteOverRestBias.compareTo(0f) > 0 && noteOverRestBias.compareTo(1f) < 0) {
                    "options.noteOverRestBias MUST BE > 0"
                }
            }

            companion object Factory {

                val default = create {
                    this.barCount = 2
                    this.noteOverRestBias = .8f
                    this.scale = Scale.default
                    this.noteLengthRange = NoteLengthRange.create(
                            Pair(NoteLength.QUARTER, 20), Pair(NoteLength.EIGHTH, 80))
                    this.program = Program.default
                }

                fun create(build: Options.() -> Unit): Options {
                    val options = Options()
                    options.build()
                    options.validateOrThrow()
                    return options
                }
            }
        }

        data class NoteLengthRange
        private constructor(val items: Map<NoteLength, Int?>): Iterable<Map.Entry<NoteLength, Int?>> {

            val size: Int
                get() = items.size

            override fun iterator() = items.iterator()

            companion object Factory {

                fun createDefault() = create(NoteLength.QUARTER, NoteLength.EIGHTH)

                fun create(vararg items: NoteLength)
                        = NoteLengthRange(items.map { Pair(it, null) }.toMap())

                fun create(vararg items: Pair<NoteLength, Int?>)
                        = NoteLengthRange(items.distinctBy { it.first }.toMap())
            }
        }
    }
}
