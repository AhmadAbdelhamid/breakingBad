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
import timber.log.Timber

class CharactersAdapter :
        PagingDataAdapter<Character, CharactersAdapter.CharacterViewHolder>(CharactersDIFFUTIL) {

    private val adapterJop = Job()
    private val scope = CoroutineScope(adapterJop + Dispatchers.Default)

    fun cancelJop() {
        adapterJop.cancel()
        Timber.d("adapterJop -> isActive: ${adapterJop.isActive} / isCancelled: ${adapterJop.isCancelled}")

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCharacterBinding.inflate(layoutInflater, parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onViewAttachedToWindow(holder: CharacterViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.startJop()
        Timber.d("${holder.name} isActive: ${holder.job.isActive} / isCancelled: ${holder.job.isCancelled}")
    }

    override fun onViewDetachedFromWindow(holder: CharacterViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.cancelJop()
        Timber.d("${holder.name} isActive: ${holder.job.isActive} / isCancelled: ${holder.job.isCancelled}")
    }

    /*******************************
                *DIFFUTIL*
     *******************************/
    object CharactersDIFFUTIL : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(
                oldItem: Character,
                newItem: Character
        ): Boolean =
                oldItem.char_id == newItem.char_id

        override fun areContentsTheSame(
                oldItem: Character,
                newItem: Character
        ): Boolean =
                oldItem == newItem
    }

    /*******************************
             *ViewHolder*
     *******************************/
    inner class CharacterViewHolder(private val binding: ItemCharacterBinding) :
            RecyclerView.ViewHolder(binding.root) {
        lateinit var job: Job
        lateinit var name: String
        private lateinit var birthday: String
        fun bind(character: Character) {
            binding.run {
                loadPhotoWithGlide(character.img)
                tvName.text = character.name
                name = character.name //for Debug

                tvNickname.text = "nickname: ${character.nickname}"
                tvStatus.text = "status: ${character.status}"
                tvBirthday.text = character.birthday
                birthday = character.birthday
                updateAge()
            }
        }

        private fun updateAge() {
            job = scope.launch {
                withContext(Dispatchers.Main) {
                    binding.tvAge.text = DateUtil.calcAge(birthday)
                    notifyItemChanged(bindingAdapterPosition)
                }
                delay(1_000)
            }
        }

        private fun loadPhotoWithGlide(url: String) {
            Glide.with(binding.ivCharacter)
                    .load(url)
                    .optionalFitCenter()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.ivCharacter)
        }

        fun startJop() {
            job.start()
        }

        fun cancelJop() {
            job.cancel()
        }
    }

}

