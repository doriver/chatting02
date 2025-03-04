
function signIn() {
    var nickname = $("#inNickname").val();
    var password = $("#inPassword").val();

    var params = {
        "nickname": nickname
        , "password": password
    }

    $.ajax({
        type:"POST",
        url:"/api/user/sign-in",
        data:JSON.stringify(params),
        contentType: 'application/json;charset=utf-8',
        success:function(response) {
            console.log(response);

            var result = response.data;
            if (result === "fail") {
                alert("로그인 실패");
            } else if (result === "success") {
                alert("로그인 성공");
                location.reload(true);
                // location.href="/view/chatting/list";
            }
        },
        error:function(xhr) {
            let response = xhr.responseJSON;
            console.log(response);
            alert("에러발생 \n" + response.errorMessage);
        }
    });
}

function signUp() {
    var nickname = $("#upNickname").val();
    var password = $("#upPassword").val();

    var params = {
        "nickname": nickname
        , "password": password
    }

    $.ajax({
        type:"POST",
        url:"/api/user/sign-up",
        data:JSON.stringify(params),
        contentType: 'application/json;charset=utf-8',
        success:function(response) {
            console.log(response);
            alert("회원가입 성공");
        },
        error:function(xhr) {
            let response = xhr.responseJSON;
            console.log(response);
            alert("에러발생 \n" + response.errorMessage);
        }
    });
}
 

$(document).ready(function() {

  

})