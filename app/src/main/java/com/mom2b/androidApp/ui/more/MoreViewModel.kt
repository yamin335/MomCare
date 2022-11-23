package com.mom2b.androidApp.ui.more

import android.content.Context
import com.mom2b.androidApp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    @ApplicationContext private val application: Context
) : BaseViewModel(application) {
//    val defaultResponse: MutableLiveData<DefaultResponse> = MutableLiveData()
//
//    val allBooks: MutableLiveData<List<ClassWiseBook>> by lazy {
//        MutableLiveData<List<ClassWiseBook>>()
//    }
//
//    val allBooksFromDB: LiveData<List<ClassWiseBook>> = liveData {
//        bookChapterDao.getAllBooks().collect { list ->
//            emit(list)
//        }
//    }
//
//    fun saveBooksInDB(books: List<ClassWiseBook>) {
//        try {
//            val handler = CoroutineExceptionHandler { _, exception ->
//                exception.printStackTrace()
//            }
//
//            viewModelScope.launch(handler) {
//                bookChapterDao.updateBooks(books)
//            }
//        } catch (e: SQLiteException) {
//            e.printStackTrace()
//        }
//    }
//
//    val slideDataList: MutableLiveData<List<AdSlider>> by lazy {
//        MutableLiveData<List<AdSlider>>()
//    }
//
//    fun getAds() {
//        if (checkNetworkStatus(true)) {
//            val handler = CoroutineExceptionHandler { _, exception ->
//                exception.printStackTrace()
//                apiCallStatus.postValue(ApiCallStatus.ERROR)
//                toastError.postValue(AppConstants.serverConnectionErrorMessage)
//            }
//
//            apiCallStatus.postValue(ApiCallStatus.LOADING)
//            viewModelScope.launch(handler) {
//                when (val apiResponse = ApiResponse.create(mediaRepository.getAdsRepo())) {
//                    is ApiSuccessResponse -> {
//                        apiCallStatus.postValue(ApiCallStatus.SUCCESS)
//                        slideDataList.postValue(apiResponse.body.data?.ads)
//                    }
//                    is ApiEmptyResponse -> {
//                        apiCallStatus.postValue(ApiCallStatus.EMPTY)
//                    }
//                    is ApiErrorResponse -> {
//                        apiCallStatus.postValue(ApiCallStatus.ERROR)
//                    }
//                }
//            }
//        }
//    }
//
//    fun getAcademicBooks(mobile: String, class_id: Int) {
//        if (checkNetworkStatus(false)) {
//            val handler = CoroutineExceptionHandler { _, exception ->
//                exception.printStackTrace()
//                apiCallStatus.postValue(ApiCallStatus.ERROR)
//                toastError.postValue(AppConstants.serverConnectionErrorMessage)
//            }
//
//            apiCallStatus.postValue(ApiCallStatus.LOADING)
//            viewModelScope.launch(handler) {
//                when (val apiResponse = ApiResponse.create(repository.allBookRepo(mobile, class_id))) {
//                    is ApiSuccessResponse -> {
//                        apiCallStatus.postValue(ApiCallStatus.SUCCESS)
//                        allBooks.postValue(apiResponse.body.data?.books)
//                    }
//                    is ApiEmptyResponse -> {
//                        apiCallStatus.postValue(ApiCallStatus.EMPTY)
//                    }
//                    is ApiErrorResponse -> {
//                        apiCallStatus.postValue(ApiCallStatus.ERROR)
//                    }
//                }
//            }
//        }
//    }
//
//    fun getAdminPanelBooks() {
//        if (checkNetworkStatus(false)) {
//            val handler = CoroutineExceptionHandler { _, exception ->
//                exception.printStackTrace()
//                apiCallStatus.postValue(ApiCallStatus.ERROR)
//                toastError.postValue(AppConstants.serverConnectionErrorMessage)
//            }
//
//            apiCallStatus.postValue(ApiCallStatus.LOADING)
//            viewModelScope.launch(handler) {
//                when (val apiResponse = ApiResponse.create(repository.adminPanelBookRepo())) {
//                    is ApiSuccessResponse -> {
//                        apiCallStatus.postValue(ApiCallStatus.SUCCESS)
//                        val totalBooks = ArrayList<ClassWiseBook>()
//                        val books = apiResponse.body.data?.books
//                        books?.let {
//                            it.forEach { book ->
//                                totalBooks.add(ClassWiseBook(book.id ?: 0, book.uuid, book.name, book.title, book.authors,
//                                    book.is_paid == 1, book.book_type_id, book.price, book.status,book.logo))
//                            }
//                            allBooks.postValue(totalBooks)
//                        }
//                    }
//                    is ApiEmptyResponse -> {
//                        apiCallStatus.postValue(ApiCallStatus.EMPTY)
//                    }
//                    is ApiErrorResponse -> {
//                        apiCallStatus.postValue(ApiCallStatus.ERROR)
//                    }
//                }
//            }
//        }
//    }
//
//    fun requestBankList(type:String) {
//        if (checkNetworkStatus(true)) {
//            val handler = CoroutineExceptionHandler { _, exception ->
//                exception.printStackTrace()
//                apiCallStatus.postValue(ApiCallStatus.ERROR)
//                toastError.postValue(AppConstants.serverConnectionErrorMessage)
//            }
//
//            apiCallStatus.postValue(ApiCallStatus.LOADING)
//            Log.e("token", preferencesHelper.getAccessTokenHeader())
//            viewModelScope.launch(handler) {
//                when (val apiResponse =
//                    ApiResponse.create(repository.requestBankListRepo(type,preferencesHelper.getAccessTokenHeader()))) {
//                    is ApiSuccessResponse -> {
//                        var d=DefaultResponse(apiResponse.body.toString(), "", "", true)
//                        defaultResponse.postValue(d)
//                        apiCallStatus.postValue(ApiCallStatus.SUCCESS)
//                    }
//                    is ApiEmptyResponse -> {
//                        apiCallStatus.postValue(ApiCallStatus.EMPTY)
//                    }
//                    is ApiErrorResponse -> {
//                        Log.e("error", apiResponse.errorMessage)
//                        defaultResponse.postValue(
//                            Gson().fromJson(
//                                apiResponse.errorMessage,
//                                DefaultResponse::class.java
//                            )
//                        )
//                        apiCallStatus.postValue(ApiCallStatus.ERROR)
//                    }
//                }
//            }
//        }
//    }
}