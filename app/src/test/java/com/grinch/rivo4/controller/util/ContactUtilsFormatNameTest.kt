package com.grinch.rivo4.controller.util

import com.grinch.rivo4.modal.data.Contact
import org.junit.Assert.assertEquals
import org.junit.Test

class ContactUtilsFormatNameTest {

    private fun contact(
        name: String,
        nickname: String? = null,
        givenName: String? = null,
        familyName: String? = null
    ) = Contact(id = "1", name = name, nickname = nickname, givenName = givenName, familyName = familyName)

    @Test
    fun nicknameWinsForFirstNameFirstOrder() {
        val c = contact("Michael Smith", nickname = "Big Mike")
        assertEquals("Big Mike", ContactUtils.formatContactName(c, 0))
    }

    @Test
    fun nicknameWinsForLastNameFirstOrder() {
        val c = contact("Michael Smith", nickname = "Big Mike")
        assertEquals("Big Mike", ContactUtils.formatContactName(c, 1))
    }

    @Test
    fun blankNicknameFallsBackToName() {
        val c = contact("Michael Smith", nickname = "   ")
        assertEquals("Michael Smith", ContactUtils.formatContactName(c, 0))
    }

    @Test
    fun lastNameFirstReordersWhenNoNickname() {
        val c = contact("Michael Smith", givenName = "Michael", familyName = "Smith")
        assertEquals("Smith, Michael", ContactUtils.formatContactName(c, 1))
    }

    @Test
    fun nicknameDrivesSortKey() {
        val c = contact("Michael Smith", nickname = "Big Mike")
        assertEquals("Big Mike", ContactUtils.getContactSortKey(c, 0))
    }
}
