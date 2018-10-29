function validate(){
    var size=10 * 1024 * 1024;
    var file_size=document.getElementById('file_upload').files[0].size;
    if(file_size>=size){
        alert('Maximum file size allowed is 10MB');
        return false;
    }
}

function validateLoginform() {
    var password = document.getElementById("inputPassword");
    var confirm_password = document.getElementById("inputConfirmPassword");
    if(password.value != confirm_password.value) {
        alert("Passwords Don't Match");
        return false;
    }
}