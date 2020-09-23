package cz.vcelnicerudna

import org.mockito.ArgumentCaptor

open class BaseTest {
    @Suppress("unused")
    open fun <T> captureArg(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
}