package com.example.musicplayer.models

class Utilities {
    fun milliToTimes(milliseconds : Long) : String {
        var finalTimerString = ""
        var secondsString = ""
        val hours = (milliseconds / (1000*60*60)).toInt()
        val minutes = (milliseconds % (1000*60*60)) / (1000*60)
        val seconds = (((milliseconds % (1000*60*60)) % (1000*60)) / 1000).toInt()
        if(hours > 0) {
            finalTimerString = "$hours:"
        }
        if(seconds < 10) {
            secondsString = "0$seconds"
        }
        else {
            secondsString=seconds.toString()
        }
        finalTimerString = "$finalTimerString$minutes:$secondsString"
        return  finalTimerString
    }
//    fun getProgressPercent(currentDuration : Long, totalDuration : Long) : Int  {
//        val percentage: Double
//        val currentSeconds : Int = (currentDuration/1000).toInt()
//        val totalSeconds : Int = (totalDuration/1000).toInt()
//        percentage = ((currentSeconds.toDouble())/totalDuration)*100
//        return percentage.toInt()
//    }
    fun progressToTime(progress : Int, totalDuration: Int) : Int {
        val total = (totalDuration/1000).toInt()
        val currentDuration = (((progress.toDouble())/100)*total).toInt()
        return currentDuration*1000
    }
}