package app.tylenol.tempiot.util

/**
 * Created by baghyeongi on 2018. 4. 10..
 */
inline fun tryCatch(ifErrorDoThis : (Throwable) -> Unit = { it.printStackTrace() }, doThis : () -> Unit) {
    try {
        doThis()
    } catch (t : Throwable) {
        ifErrorDoThis(t)
    }
}