window.onload = function() {
    selectAllItems();
}

function selectAllItems() {
	var xhttp = new XMLHttpRequest();
	xhttp.open("GET", "rest/orders/");
	xhttp.onload = function() {
        var orders = JSON.parse(xhttp.responseText);
        var rows = "<tr>" +
            "<td>" + 'ID' + "</td>" +
            "<td>" + 'Customer' + "</td>" +
            "<td>" + 'Date' + "</td>" +
            "<td>" + 'Price' + "</td>" +
            "</tr>";
        for (i = 0; i < orders.length; i++) {
            rows +=
                "<tr>" +
                "<td>" + orders[i].orderID + "</td>" +
                "<td>" + orders[i].customer + "</td>" +
                "<td>" + orders[i].orderDate + "</td>" +
                "<td>" + orders[i].orderPrice + "</td>" +
                "</tr>";
        }
        console.log(rows)
        console.log(orders)
        document.getElementById("orderTable").innerHTML = rows;
    }
	xhttp.send();
}

function changeCustomer() {
    var itemToUpdate = {
		value: document.getElementById('updateId').value
	};
    var itemToUpdateJson = JSON.stringify(itemToUpdate);
    var id = document.getElementById('updateId').value;
    var xhttp = new XMLHttpRequest();
    xhttp.open('PUT', 'rest/customers/' + id);
    xhttp.setRequestHeader('Content-Type', 'application/json');
    xhttp.onload = function() {
        selectAllItems();
    }
    xhttp.send(itemToUpdateJson);
}

function deleteItem() {
    var id = document.getElementById('deleteId').value;
    var xhttp = new XMLHttpRequest();
    xhttp.open('DELETE', 'rest/orders/' + id);
    xhttp.onload = function() {
        selectAllItems();
    }
    xhttp.send();
}
