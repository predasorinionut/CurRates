package com.github.predasorinionut.currates.common

interface ModelMapper<in INPUT, out OUTPUT> {
    fun map(input: INPUT): OUTPUT
}