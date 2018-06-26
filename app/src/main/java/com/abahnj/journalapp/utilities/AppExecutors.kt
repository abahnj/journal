package me.abahnj.confession.utilities

import java.util.concurrent.Executors

private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

/** Utility method for background thread work*/

fun runOnIoThread(f : () -> Unit){
    IO_EXECUTOR.execute(f)
}