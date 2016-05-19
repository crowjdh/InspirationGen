package kr.blogspot.crowjdh.inspirationgen.music.models

/**
 * Created by Dongheyon Jeong in InspirationGen from Yooii Studios Co., LTD. on 16. 5. 19.
 *
 * Program
 *  Midi program(instrument) number.
 */

enum class Program(val title: String, val number: Int) {

    // Piano
    ACOUSTIC_GRAND_PIANO("Acoustic Grand Piano", 1),
    BRIGHT_ACOUSTIC_PIANO("Bright Acoustic Piano", 2),
    ELECTRIC_GRAND_PIANO("Electric Grand Piano", 3),
    HONKY_TONK_PIANO("Honky-tonk Piano", 4),
    ELECTRIC_PIANO_1("Electric Piano 1", 5),
    ELECTRIC_PIANO_2("Electric Piano 2", 6),
    HARPSICHORD("Harpsichord", 7),
    CLAVINET("Clavinet", 8),

    // Chromatic Percussion
    CELESTA("Celesta", 9),
    GLOCKENSPIEL("Glockenspiel", 10),
    MUSIC_BOX("Music Box", 11),
    VIBRAPHONE("Vibraphone", 12),
    MARIMBA("Marimba", 13),
    XYLOPHONE("Xylophone", 14),
    TUBULAR_BELLS("Tubular Bells", 15),
    DULCIMER("Dulcimer", 16),

    // Organ
    DRAWBAR_ORGAN("Drawbar Organ", 17),
    PERCUSSIVE_ORGAN("Percussive Organ", 18),
    ROCK_ORGAN("Rock Organ", 19),
    CHURCH_ORGAN("Church Organ", 20),
    REED_ORGAN("Reed Organ", 21),
    ACCORDION("Accordion", 22),
    HARMONICA("Harmonica", 23),
    TANGO_ACCORDION("Tango Accordion", 24),

    // Guitar
    ACOUSTIC_GUITAR_NYLON("Acoustic Guitar (nylon, 25)", 25),
    ACOUSTIC_GUITAR_STEEL("Acoustic Guitar (steel, 26)", 26),
    ELECTRIC_GUITAR_JAZZ("Electric Guitar (jazz, 27)", 27),
    ELECTRIC_GUITAR_CLEAN("Electric Guitar (clean, 28)", 28),
    ELECTRIC_GUITAR_MUTED("Electric Guitar (muted, 29)", 29),
    OVERDRIVEN_GUITAR("Overdriven Guitar", 30),
    DISTORTION_GUITAR("Distortion Guitar", 31),
    GUITAR_HARMONICS("Guitar Harmonics", 32),

    // Bass
    ACOUSTIC_BASS("Acoustic Bass", 33),
    ELECTRIC_BASS_FINGER("Electric Bass (finger, 34)", 34),
    ELECTRIC_BASS_PICK("Electric Bass (pick, 35)", 35),
    FRETLESS_BASS("Fretless Bass", 36),
    SLAP_BASS_1("Slap Bass 1", 37),
    SLAP_BASS_2("Slap Bass 2", 38),
    SYNTH_BASS_1("Synth Bass 1", 39),
    SYNTH_BASS_2("Synth Bass 2", 40),

    // String
    VIOLIN("Violin", 41),
    VIOLA("Viola", 42),
    CELLO("Cello", 43),
    CONTRABASS("Contrabass", 44),
    TREMOLO_STRINGS("Tremolo Strings", 45),
    PIZZICATO_STRINGS("Pizzicato Strings", 46),
    ORCHESTRAL_HARP("Orchestral Harp", 47),
    TIMPANI("Timpani", 48),

    // Ensemble
    STRING_ENSEMBLE_1("String Ensemble 1", 49),
    STRING_ENSEMBLE_2("String Ensemble 2", 50),
    SYNTH_STRINGS_1("Synth Strings 1", 51),
    SYNTH_STRINGS_2("Synth Strings 2", 52),
    CHOIR_AAHS("Choir Aahs", 53),
    VOICE_OOHS("Voice Oohs", 54),
    SYNTH_CHOIR("Synth Choir", 55),
    ORCHESTRA_HIT("Orchestra Hit", 56),

    // Brass
    TRUMPET("Trumpet", 57),
    TROMBONE("Trombone", 58),
    TUBA("Tuba", 59),
    MUTED_TRUMPET("Muted Trumpet", 60),
    FRENCH_HORN("French Horn", 61),
    BRASS_SECTION("Brass Section", 62),
    SYNTH_BRASS_1("Synth Brass 1", 63),
    SYNTH_BRASS_2("Synth Brass 2", 64),

    // Reed
    SOPRANO_SAX("Soprano Sax", 65),
    ALTO_SAX("Alto Sax", 66),
    TENOR_SAX("Tenor Sax", 67),
    BARITONE_SAX("Baritone Sax", 68),
    OBOE("Oboe", 69),
    ENGLISH_HORN("English Horn", 70),
    BASSOON("Bassoon", 71),
    CLARINET("Clarinet", 72),

    // Pipe
    PICCOLO("Piccolo", 73),
    FLUTE("Flute", 74),
    RECORDER("Recorder", 75),
    PAN_FLUTE("Pan Flute", 76),
    BLOWN_BOTTLE("Blown bottle", 77),
    SHAKUHACHI("Shakuhachi", 78),
    WHISTLE("Whistle", 79),
    OCARINA("Ocarina", 80),

    // Synth Lead
    LEAD_1_SQUARE("Lead 1 (square, 81)", 81),
    LEAD_2_SAWTOOTH("Lead 2 (sawtooth, 82)", 82),
    LEAD_3_CALLIOPE("Lead 3 (calliope, 83)", 83),
    LEAD_4_CHIFF("Lead 4 chiff", 84),
    LEAD_5_CHARANG("Lead 5 (charang, 85)", 85),
    LEAD_6_VOICE("Lead 6 (voice, 86)", 86),
    LEAD_7_FIFTHS("Lead 7 (fifths, 87)", 87),
    LEAD_8_BASS_PLUS_LEAD("Lead 8 (bass + lead, 88)", 88),

    // Synth Pad
    PAD_1_NEW_AGE("Pad 1 (new age, 89)", 89),
    PAD_2_WARM("Pad 2 (warm, 90)", 90),
    PAD_3_POLYSYNTH("Pad 3 (polysynth, 91)", 91),
    PAD_4_CHOIR("Pad 4 (choir, 92)", 92),
    PAD_5_BOWED("Pad 5 (bowed, 93)", 93),
    PAD_6_METALLIC("Pad 6 (metallic, 94)", 94),
    PAD_7_HALO("Pad 7 (halo, 95)", 95),
    PAD_8_SWEEP("Pad 8 (sweep, 96)", 96),

    // Synth Effects
    FX_1_RAIN("FX 1 (rain, 97)", 97),
    FX_2_SOUNDTRACK("FX 2 (soundtrack, 98)", 98),
    FX_3_CRYSTAL("FX 3 (crystal, 99)", 99),
    FX_4_ATMOSPHERE("FX 4 (atmosphere, 100)", 100),
    FX_5_BRIGHTNESS("FX 5 (brightness, 101)", 101),
    FX_6_GOBLINS("FX 6 (goblins, 102)", 102),
    FX_7_ECHOES("FX 7 (echoes, 103)", 103),
    FX_8_SCIFI("FX 8 (sci-fi, 104)", 104),

    // Ethnic
    SITAR("Sitar", 105),
    BANJO("Banjo", 106),
    SHAMISEN("Shamisen", 107),
    KOTO("Koto", 108),
    KALIMBA("Kalimba", 109),
    BAGPIPE("Bagpipe", 110),
    FIDDLE("Fiddle", 111),
    SHANAI("Shanai", 112),

    // Percussive
    TINKLE_BELL("Tinkle Bell", 113),
    AGOGO("Agogo", 114),
    STEEL_DRUMS("Steel Drums", 115),
    WOODBLOCK("Woodblock", 116),
    TAIKO_DRUM("Taiko Drum", 117),
    MELODIC_TOM("Melodic Tom", 118),
    SYNTH_DRUM("Synth Drum", 119),
    REVERSE_CYMBAL("Reverse Cymbal", 120),

    // Sound effects
    GUITAR_FRET_NOISE("Guitar Fret Noise", 121),
    BREATH_NOISE("Breath Noise", 122),
    SEASHORE("Seashore", 123),
    BIRD_TWEET("Bird Tweet", 124),
    TELEPHONE_RING("Telephone Ring", 125),
    HELICOPTER("Helicopter", 126),
    APPLAUSE("Applause", 127),
    GUNSHOT("Gunshot", 128);

    companion object Factory {
        val default = ACOUSTIC_GRAND_PIANO
    }
}
