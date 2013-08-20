
wheel1 = 'THRELILICHATUDORSAEJENEBCR' 
wheel2 = 'WLENROHYROUIDEDJDVRFUAHWOI'
wheel3 = 'OEEUESWTHYEEDIDUIWOEROENWL'
wheel4 = 'CEUWNYDAORICYOINAYENTHCRYO'
wheels = [wheel1, wheel2, wheel3, wheel4]
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

getChar = { int wheelNumber, int offset ->
    offset = offset % 26
    wheels[wheelNumber-1].charAt(offset)
}.memoize()

wordSets = []
getWord = { column, offsets ->
    word = "" + getChar(1, column)
    offsets.eachWithIndex { offset, index ->
        word += getChar(index + 2, column + offset)
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

groups = uniqueWords.groupBy { words ->
    if(!words.find { Character.isLowerCase(it.charAt(0)) }) {
        return 'fromInside'
    } else if(!words.find { Character.isUpperCase(it.charAt(0)) }) {
        return 'fromOutside'
    } else {
        return 'mixed'
    }
}
new File("secret_message_2.txt").withPrintWriter { writer ->
    writer.println "Wheel 1: ${wheel1} (the smallest wheel)"
    writer.println "Wheel 2: ${wheel2}"
    writer.println "Wheel 3: ${wheel3}"
    writer.println "Wheel 4: ${wheel4} (the largest wheel)"
    writer.println ''
    fromInside = groups['fromInside']?.sort { it.size() }
    if(fromInside) {
        writer.println """
Number of wheel positions which include combinations of words
beginning on the smallest wheel: ${fromInside.size()}
"""
        fromInside.eachWithIndex { words, idx ->
            writer.printf "%4d: ${words*.toUpperCase()}\n", idx + 1
        }
    } else {
        writer.println '''
No wheel positions were found that only include words beginning
on the smallest wheel.
'''
    }

    fromOutside = groups['fromOutside']?.sort { it.size() }
    if(fromOutside) {
        writer.println """
Number of wheel positions which include combinations of words
beginning on the largest wheel: ${fromOutside.size()}
"""
        fromOutside.eachWithIndex { words, idx ->
            writer.printf "%4d: ${words*.toUpperCase()}\n", idx + 1
        }
    } else {
        writer.println '''
No wheel positions were found that only include words beginning
on the largest wheel.
'''
    }

    mixed = groups['mixed']?.sort { it.size() }
    if(mixed) {
        writer.println """
Number of wheel positions which include combinations of words
beginning on the largest wheel and the smallest wheel: ${mixed.size()}

In the list below, lower case words begin on the largest wheel and
upper case words begin on the smallest wheel.
"""
        mixed.eachWithIndex { words, idx ->
            writer.printf "%4d: $words\n", idx + 1
        }
    } else {
        writer.println '''
No wheel positions were found that include combinations of words beginning
on the largest wheel and the smallest wheel.
'''
    }
    /*
    uniqueWords.sort { it.size() }.eachWithIndex { words, idx ->
        writer.printf "%4d: $words\n", idx + 1
    }
    */
}

