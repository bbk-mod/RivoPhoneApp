package com.grinch.rivo4.modal.data

import org.junit.Assert.assertEquals
import org.junit.Test

class ContactDisplayNameTest {
    private fun contact(name: String, nickname: String? = null) =
        Contact(id = "1", name = name, nickname = nickname)

    @Test
    fun nicknameReplacesFullName() {
        assertEquals("Big Mike", contact("Michael Smith", "Big Mike").displayName)
    }

    @Test
    fun fallsBackToFullNameWhenNicknameMissing() {
        assertEquals("Michael Smith", contact("Michael Smith").displayName)
    }

    @Test
    fun fallsBackToFullNameWhenNicknameBlank() {
        assertEquals("Michael Smith", contact("Michael Smith", "   ").displayName)
    }

    @Test
    fun trimsNicknameWhitespace() {
        assertEquals("Mike", contact("Michael Smith", "  Mike  ").displayName)
    }
}
