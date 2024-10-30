package com.mogun.mogunchatting.chatlist

data class ChatRoomItem(
    val chatRoomId: String,
    val otherUserName: String,
    val lastMessage: String,
)