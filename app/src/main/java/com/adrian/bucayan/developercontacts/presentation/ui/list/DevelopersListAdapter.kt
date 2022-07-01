package com.adrian.bucayan.developercontacts.presentation.ui.list

import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.adrian.bucayan.developercontacts.R
import com.adrian.bucayan.developercontacts.databinding.DevelopersItemsBinding
import com.adrian.bucayan.developercontacts.domain.model.Developer
import com.adrian.bucayan.developercontacts.presentation.ui.util.adapter.BaseRecyclerViewAdapter
import com.adrian.bucayan.developercontacts.presentation.ui.util.adapter.BaseViewHolder
import com.adrian.bucayan.developercontacts.presentation.ui.util.adapter.ViewHolderInitializer
import kotlinx.coroutines.ExperimentalCoroutinesApi

class DeveloperListAdapter : BaseRecyclerViewAdapter<Developer, DevelopersItemsBinding>() ,
    ViewHolderInitializer<Developer, DevelopersItemsBinding> {

    var toSelectDeveloper: ((Developer, Int) -> Unit)? = null

    init { viewBindingInitializer = this }

    class TopSpacingDecoration(private val padding: Int): RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)

            if (parent.getChildAdapterPosition(view) > 0) {
                outRect.top = padding
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun generateViewHolder(parent: ViewGroup): BaseViewHolder<Developer, DevelopersItemsBinding> {

        val itemBinding: DevelopersItemsBinding = DevelopersItemsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return DeveloperListAdapterViewHolder(itemBinding, toSelectDeveloper)

    }

}

@ExperimentalCoroutinesApi
class DeveloperListAdapterViewHolder(
    viewBinding: DevelopersItemsBinding,
    private val toSelectDeveloper: ((Developer, Int) -> Unit)? ) : BaseViewHolder<Developer, DevelopersItemsBinding>(viewBinding) {

    private val holder : LinearLayout = viewBinding.llDeveloperItemsHolder
    private val photo: ImageView = viewBinding.tvDeveloperPhoto
    private val name : TextView = viewBinding.tvDeveloperName
    private val email : TextView = viewBinding.tvDeveloperEmail
    private val phone : TextView = viewBinding.tvDeveloperPhone
    private val company : TextView = viewBinding.tvDeveloperCompany

    @SuppressLint("SetTextI18n", "ResourceType")
    override fun setViews(item: Developer) {
        super.setViews(item)

        photo.load(item.photo) {
            crossfade(true)
            placeholder(R.drawable.img)
            scale(Scale.FILL)
        }

        name.text = item.name
        email.text = "Email : " + item.email
        phone.text = "Phone : " + item.phoneNumber
        company.text = "Company : " + item.companyName
        
        holder.setOnClickListener{
            toSelectDeveloper?.invoke(item, adapterPosition)
        }

    }



}

