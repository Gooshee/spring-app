var BASE_URL = "http://@service-address@:8081/";

var springApp = angular.module('springApp', []);
springApp.controller('ClienteController', ['$scope', '$http', '$interpolate', function ($scope, $http, $interpolate) {
	$scope.initialState = true;
	$scope.saveError = false;
	$scope.clientToRemove = {};
	$scope.cliente = {};
	$scope.clientes = {};

	$scope.$watch(function () {
		return $scope.cliente.nome;
	}, function (newValue, oldValue) {
		$scope.saveError = false;
	});

	$scope.$watch(function () {
		return $scope.cliente.cpf;
	}, function (newValue, oldValue) {
		$scope.saveError = false;
	});

	// Saving a client
	this.save = function () {
		if ($scope.cliente.nome != null && $scope.cliente.cpf != null) {
			$http.post(BASE_URL + "save", angular.toJson($scope.cliente)).then(this.handleSaveResponse, this.handleErrorResponse);
		}
	};

	this.handleSaveResponse = function (response) {
		$scope.clientes.push(response.data);
		$scope.cliente = {};
		$scope.appForm.nome.$setUntouched(true);
		$scope.appForm.cpf.$setUntouched(true);
		$scope.initialState = false;
		$scope.saveError = false;
		$("#infoMessage").text($scope.messages["msg.cliente.salvo.sucesso"]);
	};

	this.handleErrorResponse = function (response) {
		var data = response.data;
		console.error(data.error + " - " + data.exception);
		$scope.saveError = true;
		$("#errorMessage").text($scope.messages["msg.erro.cadastro.cliente"]);
	};

	// Removing a client
	this.remove = function () {
		$http({
			method: 'POST',
			url: BASE_URL + "remove",
			data: $.param({id: $scope.clientToRemove.identifier}),
			headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		});
		for (var i = 0; i < $scope.clientes.length; i++) {
			if ($scope.clientes[i].identifier == $scope.clientToRemove.identifier) {
				$scope.clientes.splice(i, 1);
				$scope.clientToRemove = null;
				break;
			}
		}
		$scope.initialState = false;
		$('#deleteModal').modal('hide');
		$("#infoMessage").text($scope.messages["msg.cliente.removido.sucesso"]);
	};

	this.openRemoveModal = function (cliente) {
		$scope.clientToRemove = cliente;
		$scope.clientName = cliente.nome;
		var text = $scope.messages["modal.remocao.msg"];
		$("#deleteModalText").text($interpolate(text)($scope));
		$scope.clientName = null;
	};

	this.count = function (cpf, def) {
		$http({method: "GET", url: BASE_URL + "count/" + cpf}).then(function (response) {
			if (response.data > 0) {
				// Async reject response
				def.reject();
			} else {
				// Async accept response
				def.resolve();
			}
		}, null);
	};

	// Searching for all clients
	this.findAll = function () {
		$http({method: "GET", url: BASE_URL + "clientes"}).then(function (response) {
			$scope.clientes = response.data;
		}, null);
	};

	this.findAll();
}]);

// Internationalizatiton (i18n)
springApp.controller('LanguageController', ['$scope', '$http', function ($scope, $http) {
	$scope.locale = "pt_BR";
	$scope.messages = {};

	this.changeLanguage = function (locale) {
		$scope.locale = locale;
		this.reloadMessages();
	};

	this.reloadMessages = function () {
		$http({
			method: 'GET',
			url: BASE_URL + "resources/messages/messages_" + $scope.locale + ".json"
		}).then(function (response) {
			$scope.messages = response.data;
		}, null);
	};

	this.reloadMessages();
}]);

// Only number directive
springApp.directive('onlyNum', function () {
	return function (scope, element, attrs) {
		var keyCode = [8, 9, 37, 39, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 110];
		element.bind("keydown", function (event) {
			if ($.inArray(event.which, keyCode) == -1) {
				event.preventDefault();
			}
		});
	};
});

// CPF Validator
springApp.directive('validaCpf', function () {
	return {
		require: 'ngModel',
		link: function (scope, elem, attrs, ctrl) {
			ctrl.$validators.validaCpf = function (modelValue, viewValue) {
				if (ctrl.$isEmpty(modelValue)) {
					return false;
				}
				return validaCPF(modelValue);
			};
		}
	};
});

// CPF Avalaibility Validator
springApp.directive('checkCpfAvailability', function ($q) {
	return {
		require: 'ngModel',
		link: function (scope, elem, attrs, ctrl) {
			ctrl.$asyncValidators.checkCpfAvailability = function (modelValue, viewValue) {
				// Async call
				var def = $q.defer();
				if (ctrl.$isEmpty(modelValue)) {
					def.reject();
					return def.promise;
				}
				scope.controller.count(modelValue, def);
				// Returning a promise
				return def.promise;
			};
		}
	};
});

function validaCPF(cpf) {
	if (cpf.length != 11) {
		return false;
	}

	// Elimina CPFs invalidos conhecidos
	var firstChar = cpf.charAt(0);
	var cpfInvalido = "";
	for (var k = 0; k < 11; k++) {
		cpfInvalido += firstChar;
	}

	if (cpf == cpfInvalido) {
		return false;
	}

	// Valida 1o digito
	var add = 0;
	for (var i = 0; i < 9; i++) {
		add += parseInt(cpf.charAt(i)) * (10 - i);
	}

	var rev = 11 - (add % 11);
	if (rev == 10 || rev == 11) {
		rev = 0;
	}

	if (rev != parseInt(cpf.charAt(9))) {
		return false;
	}

	// Valida 2o digito
	add = 0;
	for (var j = 0; j < 10; j++) {
		add += parseInt(cpf.charAt(j)) * (11 - j);
	}

	rev = 11 - (add % 11);
	if (rev == 10 || rev == 11) {
		rev = 0;
	}

	return rev == parseInt(cpf.charAt(10));
}