package ca.tetervak.Eventdata.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("price")
fun bindPrice(textView: TextView, price: Float?){
    if(price is Float){
        textView.text = String.format("$%04.2f", price)
    }
}