package com.mogun.mogunchatting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.mogun.mogunchatting.Key.Companion.DB_USERS
import com.mogun.mogunchatting.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일 또는 패스워드를 입력해주십시오.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 회원 생성
            Firebase.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // 회원가입 성공
                        Toast.makeText(this, "회원가입에 성공하였습니다 로그인을 진행해주세요.", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        // 회원가입 실패
                        Toast.makeText(this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.signInButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일 또는 패스워드를 입력해주십시오.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 로그인
            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    val currentUser = Firebase.auth.currentUser

                    if (task.isSuccessful && currentUser != null) {
                        val userId = currentUser.uid
                        val user = mutableMapOf<String, Any>()
                        user["userId"] = userId
                        user["username"] = email

                        Firebase.database.reference.child(DB_USERS).child(userId).updateChildren(user)

                        // 로그인 성공
                        startActivity(
                            Intent(this, MainActivity::class.java)
                        )
                        finish()
                    } else {
                        // 로그인 실패
                        Log.e("LoginActivity", "로그인 실패", task.exception)
                        Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}