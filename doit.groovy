
wordSize = 4
words = [] as Set
prefixes = [] as Set
postfixes = [] as Set
new File('words.txt').eachLine { line ->
    line = line.trim().toUpperCase()
    if(line.endsWith('%')) line = line[0..-2]
    if(line?.size() == wordSize) {
        words << line
        (1..(wordSize-2)).each {
            prefixes << line[0..it]
            postfixes << line[-1-it..-1]
        }
    }
}

// A = 65
// Z = 90
getChar = { int offset ->
    c = 65 + offset
    if(c > 90) c -= 26
    (char)c
}.memoize()

wordSets = []
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
            foundWords = [] 
            for(int column = 0; column < 26; column++) {
                word = getWord(column, newOffsets)
                if(words.contains(word)) {
                    foundWords << word
                } else if(words.contains(word.reverse())) {
                    foundWords << word.reverse().toLowerCase()
                }
            }
            if(foundWords.size() > 1) {
                sortedFoundWords = foundWords.sort { it.toLowerCase() }
                //if(Character.isLowerCase(sortedFoundWords[0][0] as char)) {
                    wordSets << foundWords.sort { it.toLowerCase() }
                //}
            }
        }
    } else if(offsets.size < wordSize -2) {
        for(int newOffset = 0; newOffset < 26; newOffset++) {
            matchCount = 0
            newOffsets = offsets + newOffset
            for(int column = 0; column < 26 && matchCount < 2; column++) {
                if(prefixes.contains(getWord(column, newOffsets))) {
                    matchCount++
                } else if(postfixes.contains(getWord(column, newOffsets))) {
                    matchCount++
                }
            }
            if(matchCount > 1) {
                println "$wordSize -> ${newOffsets}"
                processOffsets newOffsets
            }
        }
    }
}
processOffsets ([])

comprator = { a, b -> 
}
wordSets = wordSets.sort{it[0].toLowerCase()}
uniqueWords = new ArrayList(wordSets).findAll { list ->
    if(Character.isLowerCase(list[0][0] as char)) {
        return true
    }
    match = wordSets.find {
        it*.toLowerCase() == list*.toLowerCase()
    }
    if(match) {
        return false
    } else {
        return true
    }
}

new File("results_${wordSize}.txt").withPrintWriter { writer ->
uniqueWords.sort { it.size() }.eachWithIndex { words, idx ->
    writer.printf "%4d: $words\n", idx + 1
}
}

