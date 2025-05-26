package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.data.db.AppDataBase
import com.example.playlistmaker.data.network.ITunesApi
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.data.network.RetrofitClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val dataModule = module {

    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    single<NetworkClient> {
        RetrofitClient(get(), get())
    }

    single {
        androidContext()
            .getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
    }


    factory<MediaPlayer> {
        MediaPlayer()
    }

    single {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "database.db")
            .fallbackToDestructiveMigration(false)
            .build()
    }


}


