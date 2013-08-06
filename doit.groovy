
(9..12).each { wordSize ->
words = [] as Set
prefixes = [] as Set
new File('words.txt').eachLine { line ->
    line = line.trim().toUpperCase()
    if(line?.size() == wordSize) {
        words << line
        (1..(wordSize-2)).each {
            prefixes << line[0..it]
        }
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
getWord = { column, offsets ->
    word = "" + getChar(column)
    offsets.each { offset ->
        word += getChar(column + offset)
    }
    word
}
processOffsets = null
processOffsets = { offsets ->
    if(offsets.size() == wordSize -2 ) {
        for(int newOffset = 0; newOffset < 26; newOffset++) {
            newOffsets = offsets + newOffset
            foundWords = [] as SortedSet
            for(int column = 0; column < 26; column++) {
                word = getWord(column, newOffsets)
                if(words.contains(word)) {
                    foundWords << word
                }
            }
            if(foundWords.size() > 1) {
                wordSets << foundWords
            }
        }
    } else if(offsets.size < wordSize -2) {
        for(int newOffset = 0; newOffset < 26; newOffset++) {
            matchCount = 0
            newOffsets = offsets + newOffset
            for(int column = 0; column < 26 && matchCount < 2; column++) {
                if(prefixes.contains(getWord(column, newOffsets))) {
                    matchCount++
                }
            }
            if(matchCount > 1) {
                println newOffsets
                processOffsets newOffsets
            }
        }
    }
}
processOffsets ([])

new File("results_${wordSize}.txt").withPrintWriter { writer ->
wordSets.sort { it[0] }.sort { it.size() }.eachWithIndex { words, idx ->
    writer.printf "%4d: $words\n", idx
}
}
}
