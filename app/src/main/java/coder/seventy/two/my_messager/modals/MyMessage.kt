package coder.seventy.two.my_messager.modals

data class MyMessage(val id: String, val text: String, val fromId: String, val toId: String, val timestamp: Long){
    constructor() : this ("","","","",-1)
}