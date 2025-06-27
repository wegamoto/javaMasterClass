SELECT * FROM users WHERE username = 'wegamoto' OR email = 'wegamoto';

SELECT * FROM cart_items ci
JOIN users u ON ci.user_id = u.id
WHERE u.username = 'wegamoto' OR u.email = 'wegamoto';


SELECT * FROM cart_items WHERE user_id IS NULL;

SELECT * FROM cart WHERE user_id IS NOT NULL;