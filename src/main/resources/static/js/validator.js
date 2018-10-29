function validate(){
        var size=10 * 1024 * 1024;
        var file_size=document.getElementById('file_upload').files[0].size;
        if(file_size>=size){
            alert('File too large');
            return false;
        }
}