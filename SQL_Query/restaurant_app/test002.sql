SELECT * FROM restaurant_db.categories;

INSERT INTO categories (id, name) VALUES (1, 'อาหารไทย');


SELECT CONSTRAINT_NAME, TABLE_NAME, COLUMN_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME
FROM information_schema.KEY_COLUMN_USAGE
WHERE TABLE_NAME = 'menu_items' AND COLUMN_NAME = 'category_id';

ALTER TABLE menu_items DROP FOREIGN KEY FK5bg0vbmql5ggu48n7d5pwgjg3;
ALTER TABLE menu_items ADD CONSTRAINT FK_category
FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL;


