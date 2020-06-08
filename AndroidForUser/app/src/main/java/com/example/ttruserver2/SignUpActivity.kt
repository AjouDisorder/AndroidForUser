package com.example.ttruserver2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.ttruserver2.Retrofit.IMyService
import com.example.ttruserver2.Retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_sign_up.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    lateinit var iMyService: IMyService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Init API
        val retrofit = RetrofitClient.getInstance()
        iMyService = retrofit.create(IMyService::class.java)

        signup_complete_button.setOnClickListener {
            val userId = signup_email_area.text.toString()
            val password = signup_password_area.text.toString()
            val name = signup_name_area.text.toString()

            //Check empty
            if(TextUtils.isEmpty(userId)){
                Toast.makeText(this,"Email을 입력하세요", Toast.LENGTH_SHORT).show()
            }
            if(TextUtils.isEmpty(password)){
                Toast.makeText(this,"Password를 입력하세요", Toast.LENGTH_SHORT).show()
            }
            if(TextUtils.isEmpty(name)){
                Toast.makeText(this,"이름을 입력하세요", Toast.LENGTH_SHORT).show()
            }else{
                iMyService.signUpUser(userId, password, name).enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        Toast.makeText(this@SignUpActivity, "$t", Toast.LENGTH_LONG).show()
                    }
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>?) {
                        val res = response?.body()?.string()
                        val jsonObject = JSONObject(res)
                        val result = jsonObject.getString("result")
                        if(result == "userId is duplicated!"){
                            Toast.makeText(this@SignUpActivity, "중복된 아이디 입니다", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@SignUpActivity, "$result", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                })
            }
        }
    }
}
