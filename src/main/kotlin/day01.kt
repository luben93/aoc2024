package com.postnord.mtc.receiptlist

import println
import readInput

fun part1(lines: List<String>): Int {
    var left = lines.map { l -> Integer.parseInt(l.split("   ").first()) }.sorted()
    var right = lines.map { l -> Integer.parseInt(l.split("   ").last()) }.sorted()
    return left.mapIndexed() { index, l ->
        Math.abs(l - right[index])
    }.sum()
}

fun part2(lines: List<String>): Int {
    var left = lines.map { l -> Integer.parseInt(l.split("   ").first()) }
    var right: Map<Int, List<Int>> = lines.map { l -> Integer.parseInt(l.split("   ").last()) }
        .groupBy { it }

    return left.map { l ->
        l * right.getOrDefault(l, emptyList()).size
    }.sum()
}

fun main() {
    //todo 2 lists, sort, for each find diff and sum
    check(part1(readInput("Day01_test")) == 11)
    check(part2(readInput("Day01_test")) == 31)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}