function openChangePanel() {
    document.getElementById("changeDiv").style.display = "block";
}

function closeChangePanel() {
    document.getElementById("updateId").value = "";
    document.getElementById("changeDiv").style.display = "none";
}

function openDeletePanel() {
    document.getElementById("deleteDiv").style.display = "block";
}

function closeDeletePanel() {
    document.getElementById("deleteId").value = "";
    document.getElementById("deleteDiv").style.display = "none";
}
