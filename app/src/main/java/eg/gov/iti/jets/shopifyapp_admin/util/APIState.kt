package eg.gov.iti.jets.shopifyapp_admin.util

sealed class APIState<out T>{

    class Success<T>(var data : T): APIState<T>()
    class Loading(val message:String = "loading"): APIState<Nothing>()
    class Error(val error:String="Error"): APIState<Nothing>()
}