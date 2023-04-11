package chess

class Player(numberPlayer: String) {
    init {
        println("$numberPlayer Player's name:")
    }
    val name = readln()
    val listPawns = MutableList(8){""}
    init {
        var letter = 'a'
        for (i in 0..7) {
            listPawns[i] = "$letter${if (numberPlayer == "First") '2' else '7'}"
            letter++
        }
    }

    fun changeListPawns(move: String) {
        listPawns.remove(move.substring(0, 2))
        if (move.length > 2) listPawns.add(move.substring(2))
    }
}
