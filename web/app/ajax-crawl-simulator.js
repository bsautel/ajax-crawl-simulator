'use strict';


function SimulationsContoller($scope, $http) {
    $scope.refresh = function () {
        $http.get('/simulations').success(function (simulations) {
            $scope.simulations = simulations;
        });
    };
    $scope.refresh();

    $scope.view_simulation_link = function view_simulation_link(simulation) {
        return '#' + simulationsRoute + "/" + simulation.name;
    };

    $scope.createSimulation = function () {
        $scope.newSimulation = {};
    };

    $scope.cancelSimulationCreation = function () {
        delete $scope.newSimulation;
    };

    $scope.addNewSimulation = function () {
        $http.post('/simulations', $scope.newSimulation).success(function (simulation) {
            $scope.cancelSimulationCreation();
            $scope.refresh();
        });
    };

    $scope.isNewSimulationValid = function() {
        if ($scope.newSimulation) {
            var newSimulation = $scope.newSimulation;
            return newSimulation.name.trim().length > 0
                && newSimulation.entryUrl.trim().length > 0
                && newSimulation.urlPrefix.trim().length > 0
        }
        return false;
    }
}

function SimulationController($routeParams, $scope, $http, $location) {
    var name = $routeParams.name;
    var simulation_url = '/simulations/' + encodeURIComponent(name);
    $http.get(simulation_url).success(function (simulation) {
        $scope.simulation = simulation;
    });
    $http.get(simulation_url + '/pages').success(function (pages) {
        $scope.pages = pages;
    });

    $scope.get_page_title = function (page) {
        if (page.type == 'HTML') {
            return page.title;
        }
        return 'Unknown title';
    };

    $scope.delete = function () {
        if (confirm('Do you want to delete this simulation?')) {
            $http.delete(simulation_url).success(function () {
                $location.path(simulationsRoute);
            });
        }

    };
}

var simulationsRoute = '/simulations';
var simulationRoute = simulationsRoute + '/:name';

angular.module('ajax-crawl-simulator', ['ngRoute']).config(
    [
        '$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when(simulationsRoute, {templateUrl: 'simulations.html', controller: SimulationsContoller}).
                when(simulationRoute, {templateUrl: 'simulation.html', controller: SimulationController}).
                otherwise({redirectTo: simulationsRoute});
        }
    ]);