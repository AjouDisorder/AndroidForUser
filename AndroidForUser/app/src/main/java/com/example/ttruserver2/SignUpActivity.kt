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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    lateinit var iMyService: IMyService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val retrofit = RetrofitClient.getInstance()
        iMyService = retrofit.create(IMyService::class.java)

        signup_complete_button.setOnClickListener {
            val userId = signup_email_area.text.toString()
            val password = signup_password_area.text.toString()
            val name = signup_name_area.text.toString()
            val dateOfBirth = signup_dateOfBirth_area.text.toString()
            val phone = signup_phone_area.text.toString()
            var sex = ""
            if (rb_male.isChecked){
                sex = "남성"
            }else if(rb_female.isChecked){
                sex = "여성"
            }

            //Check empty
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userId).matches()){
                Toast.makeText(this,"올바르지 않은 Email 형식입니다", Toast.LENGTH_SHORT).show()
            } else if(password.length < 6){
                Toast.makeText(this,"6글자 이상 Password를 입력하세요", Toast.LENGTH_SHORT).show()
            } else if(TextUtils.isEmpty(name)){
                Toast.makeText(this,"이름을 입력하세요", Toast.LENGTH_SHORT).show()
            } else if(sex == ""){
                Toast.makeText(this,"성별을 선택하세요", Toast.LENGTH_SHORT).show()
            }  else if(!validationDate(dateOfBirth)){
                Toast.makeText(this,"잘못 된 생년월일 입니다.", Toast.LENGTH_SHORT).show()
            } else if(!Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", phone)){
                Toast.makeText(this,"올바르지 않은 휴대폰번호 형식입니다", Toast.LENGTH_SHORT).show()
            } else{
                iMyService.signUpUser(userId, password, name, sex, dateOfBirth, phone).enqueue(object : Callback<ResponseBody> {
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
                            finish()
                        }
                    }
                })
            }
        }
    }

    fun validationDate(checkDate : String): Boolean {
        return try{
            val dateFormat = SimpleDateFormat("yyyyMMdd")
            dateFormat.setLenient(false)
            dateFormat.parse(checkDate)
            true
        }catch (e : ParseException){
            false
        }
    }
}
