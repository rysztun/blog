postControllers.factory('PostService', [
    '$resource', function ($resource) {
        return $resource('', {}, {

            getPosts: {
                method: 'GET',
                url: '/posts',
                isArray: true
            },

            findPost: {
                params: {id: "@id"},
                method: 'GET',
                url: '/post/:id',
                isArray: true
            },

            updatePost: {
                params: {id: "@id"},
                method: 'PUT',
                url: '/post/:id'
            },

            createPost: {
                method: 'POST',
                url: '/post'
            },

            deletePost: {
                params: {id: "@id"},
                method: 'DELETE',
                url: '/post/:id'
            }
        });
    }]);