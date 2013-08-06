
(2..6).each { wordSize ->
words = [] as Set

new File('words.txt').eachLine { line ->
    line = line.trim().toUpperCase()
    if(line?.size() == wordSize) {
        words << line
    }
}
letters = ('A'..'Z')


// A = 65
// Z = 90
getChar = { int offset ->
    c = 65 + offset
    if(c > 90) c -= 26
    (char)c
}.memoize()

wordSets = []
getWords = { offsets ->
    foundWords = [] as SortedSet
    (0..25).each { p1 ->
        word = "" + getChar(p1)
        offsets.each { offset ->
            word += getChar(p1 + offset)
        }
        if(words.contains(word)) {
            foundWords << word
        }
    }
    if(foundWords.size() > 1) {
        wordSets << foundWords
        println foundWords
    }
}

rigIt = null
rigIt = { coll ->
    if(coll.size() < wordSize - 1) {
        26.times { t ->
            rigIt(coll + t)
        }
    } else {
        getWords coll
    }
}
foo = []
rigIt foo


new File("results_${wordSize}.txt").withPrintWriter { writer ->
wordSets.sort { it[0] }.sort { it.size() }.eachWithIndex { words, idx ->
    writer.printf "%4d: $words\n", idx
}
}
}
