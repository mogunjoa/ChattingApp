package com.mogun.mogunchatting.chatlist

data class ChatRoomItem(
    val chatRoomId: String? = null,
    val lastMessage: String? = null,
    val otherUserName: String? = null,
    val otherUserId: String? = null
)