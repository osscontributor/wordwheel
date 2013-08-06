words = []
new File('words.txt').eachLine { line ->
    line = line.trim().toUpperCase()
    if(line?.size() == 4) {
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
}

getWords = { off1, off2, off3 ->
    foundWords = []
    (0..25).each { p1 ->
        word = "" + getChar(p1) + getChar(off1 + p1) + getChar(off2 + p1) + getChar(off3 + p1)
        if(words.contains(word)) {
            foundWords << word
        }
    }
    if(foundWords.size() > 1) {
        println foundWords
    }
}
(0..25).each { off1 ->
   (0..25).each { off2 ->
       (0..25).each { off3 ->
           getWords(off1, off2, off3)
       }
   }
}
