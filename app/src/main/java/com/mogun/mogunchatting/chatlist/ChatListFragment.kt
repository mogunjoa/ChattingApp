package com.mogun.mogunchatting.chatlist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mogun.mogunchatting.Key
import com.mogun.mogunchatting.R
import com.mogun.mogunchatting.chatdetail.ChatActivity
import com.mogun.mogunchatting.databinding.FragmentChatlistBinding
import com.mogun.mogunchatting.databinding.FragmentUserlistBinding

class ChatListFragment: Fragment(R.layout.fragment_chatlist) {

    private lateinit var binding: FragmentChatlistBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatlistBinding.bind(view)

        val chatListAdapter = ChatListAdapter { chatRoomItem ->
            Intent(context, ChatActivity::class.java).apply {
                putExtra(ChatActivity.EXTRA_OTHER_USER_ID, chatRoomItem.otherUserId)
                putExtra(ChatActivity.EXTRA_CHAT_ROOM_ID, chatRoomItem.chatRoomId)
                startActivity(this)
            }
        }

        binding.chatListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatListAdapter
        }

        val currentUserId = Firebase.auth.currentUser?.uid ?: return
        val chatRoomsDB = Firebase.database.reference.child(Key.DB_CHAT_ROOMS).child(currentUserId)

        chatRoomsDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chatRoomList = snapshot.children.map {
                    it.getValue(ChatRoomItem::class.java)
                }

                chatListAdapter.submitList(chatRoomList)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}