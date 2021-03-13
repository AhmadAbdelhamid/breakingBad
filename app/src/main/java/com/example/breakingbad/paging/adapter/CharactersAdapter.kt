package com.example.breakingbad.paging.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.breakingbad.databinding.ItemCharacterBinding
import com.example.model.Character
import com.example.util.DateUtil
import kotlinx.coroutines.*

class CharactersAdapter : PagingDataAdapter<Character, CharactersAdapter.PhotoViewHolder>(CharactersDIFFUTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCharacterBinding.inflate(layoutInflater, parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    object CharactersDIFFUTIL : DiffUtil.ItemCallback<com.example.model.Character>() {
        override fun areItemsTheSame(
            oldItem: com.example.model.Character,
            newItem: com.example.model.Character
        ): Boolean =
            oldItem.char_id == newItem.char_id

        override fun areContentsTheSame(
            oldItem: com.example.model.Character,
            newItem: com.example.model.Character
        ): Boolean =
            oldItem == newItem
    }

    inner class PhotoViewHolder(private val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
            binding.run {
                loadPhotoWithGlide(character.img)
                tvName.text = character.name
                tvNickname.text = "nickname: ${character.nickname}"
                tvStatus.text = "status: ${character.status}"
                tvBirthday.text = character.birthday
                GlobalScope.launch(Dispatchers.Default) {
                    delay(1_000)
                    withContext(Dispatchers.Main) {
                        tvAge.text = DateUtil.calcAge(character.birthday)
                        notifyItemChanged(adapterPosition)
                    }
                }
            }
        }

        private fun loadPhotoWithGlide(url: String) {
            Glide.with(binding.ivCharacter)
                .load(url)
                .optionalFitCenter()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.ivCharacter)
        }

    }

}

