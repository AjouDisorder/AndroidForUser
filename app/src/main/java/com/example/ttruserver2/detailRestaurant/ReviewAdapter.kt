package com.example.ttruserver2.detailRestaurant

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.ttruserver2.R
import com.example.ttruserver2.Retrofit.IMyService
import com.example.ttruserver2.Retrofit.RetrofitClient
import com.example.ttruserver2.UserData
import com.example.ttruserver2.models.ReviewModel
import kotlinx.android.synthetic.main.review_item.view.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewAdapter (val reviewList: ArrayList<ReviewModel>) : RecyclerView.Adapter<ReviewAdapter.CustomViewHolder>() {
    lateinit var iMyService: IMyService

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapter.CustomViewHolder {
        val retrofit = RetrofitClient.getInstance()
        iMyService = retrofit.create(IMyService::class.java)

        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
        return CustomViewHolder(view).apply {
            //edit review
            itemView.btn_editReview.setOnClickListener {
                itemView.tv_descriptionInReview.visibility = View.INVISIBLE
                itemView.btn_editReview.visibility = View.INVISIBLE
                itemView.btn_deleteReview.visibility = View.INVISIBLE
                itemView.btn_editComplete.visibility = View.VISIBLE
                itemView.et_reviewDescription.visibility = View.VISIBLE
                itemView.et_reviewDescription.text = itemView.tv_descriptionInReview.text as Editable?
            }
            itemView.btn_editComplete.setOnClickListener {
                itemView.tv_descriptionInReview.visibility = View.VISIBLE
                itemView.btn_editReview.visibility = View.VISIBLE
                itemView.btn_deleteReview.visibility = View.VISIBLE
                itemView.btn_editComplete.visibility = View.INVISIBLE
                itemView.et_reviewDescription.visibility = View.INVISIBLE
                itemView.tv_descriptionInReview.text = itemView.et_reviewDescription.text

                val curPos : Int = adapterPosition
                val selectedReview = reviewList[curPos]
                iMyService.updateReview(selectedReview.reviewOid, itemView.et_reviewDescription.text).enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(parent.context, "$t", Toast.LENGTH_LONG).show()
                    }
                    override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) { }
                })
            }
            //delete review
            itemView.btn_deleteReview.setOnClickListener {
                val curPos: Int = adapterPosition
                val selectedReview = reviewList[curPos]
                iMyService.deleteReview(selectedReview.reviewOid).enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(parent.context, "$t", Toast.LENGTH_LONG).show()
                    }
                    override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                        reviewList.removeAt(curPos)
                        notifyItemRemoved(adapterPosition)
                        notifyItemRangeChanged(adapterPosition, reviewList.size)
                    }
                })
            }
        }
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    override fun onBindViewHolder(holder: ReviewAdapter.CustomViewHolder, position: Int) {
        holder.userName.text = reviewList.get(position).userName
        holder.menuName.text = reviewList.get(position).menuName
        holder.description.text = reviewList.get(position).description
        holder.grade.rating = reviewList.get(position).grade.toFloat()
        //delete, edit review button visible
        if (UserData.getOid() == null){
            holder.btn_deleteReview.visibility = View.INVISIBLE
            holder.btn_editReview.visibility = View.INVISIBLE
        }else{
            iMyService.getReviewedTicketList(UserData.getOid()).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) { }
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val result = response.body()?.string()
                    val jsonArray = JSONArray(result)
                    val reviewedTicketIdList = arrayListOf<String>()
                    for (i in 0.until(jsonArray.length())){
                        val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                        reviewedTicketIdList.add(jsonObject.getString("_id"))
                    }
                    if (reviewList[position].ticket_id in reviewedTicketIdList){
                        holder.btn_deleteReview.visibility = View.VISIBLE
                        holder.btn_editReview.visibility = View.VISIBLE
                    }else{
                        holder.btn_deleteReview.visibility = View.INVISIBLE
                        holder.btn_editReview.visibility = View.INVISIBLE
                    }
                }
            })
        }

    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val userName = itemView.findViewById<TextView>(R.id.tv_userNameInReview)
        val menuName = itemView.findViewById<TextView>(R.id.tv_menuNameInReview)
        val description = itemView.findViewById<TextView>(R.id.tv_descriptionInReview)
        val grade = itemView.findViewById<RatingBar>(R.id.rb_gradeReview)
        val btn_deleteReview = itemView.findViewById<Button>(R.id.btn_deleteReview)
        val btn_editReview = itemView.findViewById<Button>(R.id.btn_editReview)
        val btn_editComplete = itemView.findViewById<Button>(R.id.btn_editComplete)
        val et_reviewDescription = itemView.findViewById<EditText>(R.id.et_reviewDescription)
    }
}