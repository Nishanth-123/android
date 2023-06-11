package com.nishanth.foody.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.nishanth.foody.data.database.RecipesEntity
import com.nishanth.foody.models.FoodRecipe
import com.nishanth.foody.util.NetworkResult
import retrofit2.Response

class RecipesBinding {

    companion object{

        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
        @JvmStatic
        fun errorImageViewVisibility(
            imageView: ImageView,
            apiResponse: NetworkResult<FoodRecipe>?,
            dataBase:List<RecipesEntity>?
            ){
            if(apiResponse is NetworkResult.Error && dataBase.isNullOrEmpty()){
                imageView.visibility= View.VISIBLE
            }else if(apiResponse is NetworkResult.Loading){
                imageView.visibility=View.INVISIBLE
            }else if(apiResponse is NetworkResult.Success){
                imageView.visibility=View.INVISIBLE
            }
        }

        @BindingAdapter("readApiResponse2", "readDatabase2", requireAll = true)
        @JvmStatic
        fun errorTextViewVisibility(
            textView: TextView,
            apiResponse: NetworkResult<FoodRecipe>?,
            dataBase:List<RecipesEntity>?
        ){
            if(apiResponse is NetworkResult.Error && dataBase.isNullOrEmpty()){
                textView.visibility= View.VISIBLE
                textView.text=apiResponse.message.toString()
            }else if(apiResponse is NetworkResult.Loading){
                textView.visibility=View.INVISIBLE
            }else if(apiResponse is NetworkResult.Success){
                textView.visibility=View.INVISIBLE
            }
        }
    }
}