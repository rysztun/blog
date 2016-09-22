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
                url: '/posts/:id'
            },

            updatePost: {
                params: {id: "@id"},
                method: 'PUT',
                url: '/posts/:id'
            },

            createPost: {
                method: 'POST',
                url: '/posts'
            },

            deletePost: {
                params: {id: "@id"},
                method: 'DELETE',
                url: '/posts/:id'
            }
        });
    }]);