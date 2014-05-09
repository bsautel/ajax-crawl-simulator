'use strict';


function SimulationsContoller($scope, $http) {
    $http.get('/simulations').success(function (simulations) {
        $scope.simulations = simulations;
    });

    $scope.view_simulation_link = function view_simulation_link(simulation) {
        return '#' + simulationsRoute + "/" + simulation.name;
    };
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