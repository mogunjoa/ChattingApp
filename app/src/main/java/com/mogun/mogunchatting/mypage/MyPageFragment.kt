package com.mogun.mogunchatting.mypage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mogun.mogunchatting.Key
import com.mogun.mogunchatting.LoginActivity
import com.mogun.mogunchatting.R
import com.mogun.mogunchatting.databinding.FragmentMypageBinding
import com.mogun.mogunchatting.userlist.UserItem

class MyPageFragment: Fragment(R.layout.fragment_mypage) {
    private lateinit var binding: FragmentMypageBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMypageBinding.bind(view)

        val currentUserId = Firebase.auth.currentUser?.uid ?:""
        val currentUserDB = Firebase.database.reference.child(Key.DB_USERS).child(currentUserId)

        currentUserDB.get().addOnSuccessListener {
            val currentUserItem = it.getValue(UserItem::class.java) ?: return@addOnSuccessListener

            binding.usernameEditText.setText(currentUserItem.username)
            binding.descriptionEditText.setText(currentUserItem.description)
        }

        binding.applyButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()

            if(username.isEmpty()) {
                Toast.makeText(context, "사용자 이름을 지정해주셔야합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = mutableMapOf<String, Any>()
            user["username"] = username
            user["description"] = description
            currentUserDB.updateChildren(user)

            Toast.makeText(context, "사용자 정보가 업데이트 되었습니다.", Toast.LENGTH_SHORT).show()
        }

        binding.signOutButton.setOnClickListener {
            Firebase.auth.signOut()
            Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()

            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }
    }
}