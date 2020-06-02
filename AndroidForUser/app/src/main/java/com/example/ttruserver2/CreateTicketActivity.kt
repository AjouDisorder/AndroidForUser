package com.example.ttruserver2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ttruserver2.Retrofit.IMyService
import com.example.ttruserver2.Retrofit.RetrofitClient
import com.example.ttruserver2.models.SearchedMenuModel
import kotlinx.android.synthetic.main.activity_create_ticket.*
import kotlinx.android.synthetic.main.menu_list_item.*

class CreateTicketActivity : AppCompatActivity() {

    lateinit var iMyService: IMyService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_ticket)

        val retrofit = RetrofitClient.getInstance()
        iMyService = retrofit.create(IMyService::class.java)
        val selectedMenu = intent.getSerializableExtra("selectedMenu") as SearchedMenuModel

        //menu data assign
        tv_menuTitleInPay.text = selectedMenu.menuTitle
        tv_totalPriceInPay.text = selectedMenu.discountedPrice.toString()
        val menuMethod = selectedMenu.method
        val availableQuantity = selectedMenu.quantity
        var quantity = 1

        //method checkbox
        if (menuMethod == "forhere"){
            rb_takeout.visibility = View.INVISIBLE
        }else if(menuMethod == "takeout"){
            rb_forhere.visibility = View.INVISIBLE
        }
        //btn quantity
        btn_plusQuantity.setOnClickListener{
            tv_ticketQuantity.text = (quantity + 1).toString()
            quantity += 1
            if (quantity == availableQuantity){
                btn_plusQuantity.isEnabled = false
            }else if(quantity > 1){
                btn_minusQuantity.isEnabled = true
            }
            tv_totalPriceInPay.text = (selectedMenu.discountedPrice * quantity).toString()
        }
        btn_minusQuantity.setOnClickListener {
            tv_ticketQuantity.text = (quantity - 1).toString()
            quantity -= 1
            if (quantity == 1){
                btn_minusQuantity.isEnabled = false
            }else if(quantity < availableQuantity){
                btn_plusQuantity.isEnabled = true
            }
            tv_totalPriceInPay.text = (selectedMenu.discountedPrice * quantity).toString()
        }

        //create ticket
        /*btn_createTicket.setOnClickListener{
            if ()
            iMyService.createTicket(selectedMenu.menuOid, quantity)
        }*/
    }
}