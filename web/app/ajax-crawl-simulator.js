'use strict';

function generateSimulationLink(simulationName) {
    return '#' + simulationsRoute + '/' + simulationName;
}

function generatePageLink(simulationName, pageUrl) {
    return generateSimulationLink(simulationName) + '/' + encodeURIComponent(encodeURIComponent(pageUrl));
}

function SimulationsContoller($scope, $http) {
    $scope.refresh = function () {
        $http.get('/simulations').success(function (simulations) {
            $scope.simulations = simulations;
        });
    };
    $scope.refresh();

    $scope.viewSimulationLink = function viewSimulationLink(simulation) {
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

    $scope.isNewSimulationValid = function () {
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
    var simulationUrl = '/simulations/' + encodeURIComponent(name);

    $http.get(simulationUrl).success(function (simulation) {
        $scope.simulation = simulation;
    });

    $http.get(simulationUrl + '/pages').success(function (pages) {
        $scope.pages = pages;
    });

    $scope.getPageTitle = function (page) {
        if (page.type == 'HTML') {
            return page.title;
        }
        return 'Unknown title';
    };

    $scope.delete = function () {
        if (confirm('Do you want to delete this simulation?')) {
            $http.delete(simulationUrl).success(function () {
                $location.path(simulationsRoute);
            });
        }
    };

    $scope.generatePageLink = function (page) {
        return generatePageLink(name, page.url);
    };
}

function PageController($routeParams, $scope, $http) {
    var simulationName = $routeParams.name;
    var pageUrl = $routeParams.url;
    var pageWsUrl = '/simulations/' + simulationName + '/pages/' + encodeURIComponent(encodeURIComponent(pageUrl));
    $http.get(pageWsUrl).success(function (page) {
        $scope.page = page;
    });

    $scope.generatePageLink = function (link) {
        return generatePageLink(simulationName, link);
    };

    $scope.generateSimulationLink = function () {
        return generateSimulationLink(simulationName);
    }
}

var simulationsRoute = '/simulations';
var simulationRoute = simulationsRoute + '/:name';
var pageRoute = simulationRoute + '/:url';

angular.module('ajax-crawl-simulator', ['ngRoute']).config(
    [
        '$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when(simulationsRoute, {templateUrl: 'simulations.html', controller: SimulationsContoller}).
                when(simulationRoute, {templateUrl: 'simulation.html', controller: SimulationController}).
                when(pageRoute, {templateUrl: 'page.html', controller: PageController}).
                otherwise({redirectTo: simulationsRoute});
        }
    ]);