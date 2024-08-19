package com.hotta.hoho.repository


import com.google.firebase.database.values
import com.hotta.hoho.utils.FireBaseAuthUtils
import com.hotta.hoho.utils.FireBaseRef
import com.hotta.hoho.view.detail.ReviewModel
import com.hotta.hoho.view.join.UserModel

class FireBaseRepository {

    // 선택한 영화의 리뷰를 저장한다.
    fun insertReview(id: String, reviewModel: ReviewModel) =
        FireBaseRef.movieReview.child(id).child(reviewModel.userid).setValue(reviewModel)

    // 사용자가 작성한 리뷰를 저장한다.
    fun insertUserReview(reviewModel: ReviewModel, id: String) =
        FireBaseRef.userReview.child(reviewModel.userid).child(id).setValue(reviewModel)

    // 회원가입시 회원정보 저장한다.
    fun insertUser(uid: String, userModel: UserModel) =
        FireBaseRef.userInfo.child(uid).setValue(userModel)

    fun insertFindUser(userModel: UserModel) =
        FireBaseRef.userFindInfo.child(userModel.name).setValue(userModel.phone)

    // 회원가입시 회원정보 저장한다.
    fun insertEmail(email: String) =
        FireBaseRef.emailCheck.child(email).setValue(email)

    fun insertLikeMovie(moveId: String) =
        FireBaseRef.movieLike.child(FireBaseAuthUtils.getUid()).child(moveId).setValue(moveId)

}
