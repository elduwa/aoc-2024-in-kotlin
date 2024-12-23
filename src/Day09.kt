fun main() {

    class File(val id : Int, var numBlocks : Int, var followingFreeBlocks : Int) {

        fun splitFile(takeNumBlocks : Int) : File {
            val newFile = File(this.id, takeNumBlocks, 0)
            this.numBlocks -= takeNumBlocks
            return newFile
        }

        override fun toString(): String {
            return "{ id: $id, numBlocks: $numBlocks, followingFreeBlocks: $followingFreeBlocks"
        }
    }

    fun transformInput(input: List<String>): MutableList<File> {
        val diskMap = (input.first() + "0").toList()
        val zippedDiskMap = mutableListOf<Pair<Int, Int>>()
        for (i in 0..diskMap.size - 2 step 2) {
            zippedDiskMap.add(diskMap[i].digitToInt() to diskMap[i + 1].digitToInt())
        }


        val rearrangedFileParts =
            zippedDiskMap.mapIndexed { idx, pair -> File(idx, pair.first, pair.second) }.toMutableList()
        return rearrangedFileParts
    }

    fun calculateChecksum(rearrangedFileParts: MutableList<File>): Long {
        var result = 0L
        var idxCursor = 0

        for (part in rearrangedFileParts) {
            val idxRange = idxCursor..<idxCursor + part.numBlocks
            result += idxRange.sumOf { it.toLong() * part.id.toLong() }
            idxCursor = idxRange.last + 1 + part.followingFreeBlocks
        }
        return result
    }

    fun part1(input: List<String>): Long {
        val rearrangedFileParts = transformInput(input)
        var cursor = 0

        while (rearrangedFileParts.size - 1 > cursor) {
            val lastOrderedFile = rearrangedFileParts[cursor]
            if (lastOrderedFile.followingFreeBlocks > 0) {
                var freeBlocks = lastOrderedFile.followingFreeBlocks

                while (freeBlocks > 0) {
                    val lastFile = rearrangedFileParts.last()
                    if (freeBlocks >= lastFile.numBlocks) {
                        rearrangedFileParts.removeLast()
                        rearrangedFileParts.add(cursor + 1, lastFile)
                        freeBlocks -= lastFile.numBlocks
                        lastFile.followingFreeBlocks = freeBlocks
                    }
                    else {
                        val splitFile = lastFile.splitFile(freeBlocks)
                        rearrangedFileParts.add(cursor + 1, splitFile)
                        freeBlocks = 0
                    }
                    rearrangedFileParts[cursor].followingFreeBlocks = 0
                    cursor++
                }
            }
            else {
                cursor++
            }
        }

        return calculateChecksum(rearrangedFileParts)
    }

    fun part2(input: List<String>): Long {
        val rearrangedFiles = transformInput(input)
        var cursor = rearrangedFiles.size - 1

        while (cursor > 0) {
            val currentFile = rearrangedFiles[cursor]
            val freeSpaceLocation =
                rearrangedFiles.slice(0..<cursor).indexOfFirst { it.followingFreeBlocks >= currentFile.numBlocks }
            if (freeSpaceLocation >= 0) {
                rearrangedFiles.removeAt(cursor)
                rearrangedFiles[cursor-1].followingFreeBlocks += (currentFile.numBlocks + currentFile.followingFreeBlocks)
                currentFile.followingFreeBlocks = rearrangedFiles[freeSpaceLocation].followingFreeBlocks - currentFile.numBlocks
                rearrangedFiles[freeSpaceLocation].followingFreeBlocks = 0
                rearrangedFiles.add(freeSpaceLocation + 1, currentFile)
            }
            else {
                cursor--
            }
        }
        val checksum = calculateChecksum(rearrangedFiles)
        return checksum
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()

}
