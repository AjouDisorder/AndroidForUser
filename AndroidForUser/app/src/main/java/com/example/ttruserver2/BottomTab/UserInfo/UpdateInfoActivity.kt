package com.example.ttruserver2.BottomTab.UserInfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.ttruserver2.MainActivity
import com.example.ttruserver2.R
import com.example.ttruserver2.Retrofit.IMyService
import com.example.ttruserver2.Retrofit.RetrofitClient
import com.example.ttruserver2.UserData
import kotlinx.android.synthetic.main.activity_update_info.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class UpdateInfoActivity : AppCompatActivity() {
    lateinit var iMyService: IMyService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_info)

        val retrofit = RetrofitClient.getInstance()
        iMyService = retrofit.create(IMyService::class.java)

        updateInfo_complete_button.setOnClickListener {
            val password = updateInfo_password_area.text.toString()
            val name = updateInfo_name_area.text.toString()
            val phone = updateInfo_phone_area.text.toString()
            var sex = ""
            if (rb_male2.isChecked){
                sex = "남성"
            }else if(rb_female2.isChecked){
                sex = "여성"
            }

            //Check empty
            if(password.length < 6){
                Toast.makeText(this,"6글자 이상 Password를 입력하세요", Toast.LENGTH_SHORT).show()
            } else if(TextUtils.isEmpty(name)){
                Toast.makeText(this,"이름을 입력하세요", Toast.LENGTH_SHORT).show()
            } else if(sex == ""){
                Toast.makeText(this,"성별을 선택하세요", Toast.LENGTH_SHORT).show()
            } else if(!Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", phone)){
                Toast.makeText(this,"올바르지 않은 휴대폰번호 형식입니다", Toast.LENGTH_SHORT).show()
            } else{
                iMyService.updateInfo(UserData.getOid(), password, name, sex, phone).enqueue(object :
                    Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        Toast.makeText(this@UpdateInfoActivity, "$t", Toast.LENGTH_LONG).show()
                    }
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>?) {
                        Toast.makeText(this@UpdateInfoActivity, "회원정보가 수정되었습니다", Toast.LENGTH_SHORT).show()
                        UserData.setPassword(password)
                        UserData.setName(name)
                        UserData.setSex(sex)
                        UserData.setPhone(phone)

                        val intent = Intent(this@UpdateInfoActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                })
            }
        }
    }
}