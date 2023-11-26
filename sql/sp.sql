# DELIMITER //
#
# CREATE PROCEDURE insert_paper_procedure(
#     IN p_tl TEXT,
#     IN p_au TEXT,
#     IN p_de TEXT,
#     IN p_wc TEXT
# )
# BEGIN
#     DECLARE tl_length INT;
#     DECLARE au_length INT;
#     DECLARE de_length INT;
#     DECLARE wc_length INT;
#
#     SET tl_length = LENGTH(p_tl);
#     SET au_length = LENGTH(p_au);
#     SET de_length = LENGTH(p_de);
#     SET wc_length = LENGTH(p_wc);
#
#     -- 根据字段大小执行不同策略
#     IF value_length <= 50 THEN
#         -- 执行策略 A，当字段大小小于等于 50 时
#         -- ...
#     ELSEIF value_length > 50 AND value_length <= 200 THEN
#         -- 执行策略 B，当字段大小在 51 到 200 之间时
#         -- ...
#     ELSE
#         -- 执行策略 C，当字段大小大于 200 时
#         -- ...
#     END IF;
# END //
#
# DELIMITER ;


-- 创建作者存储过程
DROP PROCEDURE IF EXISTS insert_or_update_author_record;

DELIMITER //

CREATE PROCEDURE insert_or_update_author_record(
    IN p_author_name VARCHAR(255),
    IN p_author_country VARBINARY(255),
    IN p_author_org VARCHAR(500),
    IN p_author_research VARBINARY(255)
)
BEGIN
    DECLARE v_count INT;

    -- 检查是否存在满足条件的记录
    SELECT author_id INTO v_count
    FROM author
    WHERE author_name = p_author_name AND author_org = p_author_org AND author_org IS NOT NULL
    LIMIT 1;

    SELECT v_count;

    IF v_count IS NULL THEN
        -- 不存在满足条件的记录，执行插入操作
        INSERT INTO author (author_name, author_country, author_org, paper_count, paper_count_per_year, research, H, high_cited_paper)
        VALUES (p_author_name, p_author_country, p_author_org, 1, NULL, p_author_research, 0, 0);
    ELSE
        -- 存在满足条件的记录，执行更新操作
        UPDATE author
        SET paper_count = paper_count + 1
        WHERE author_name = p_author_name AND author_org = p_author_org;
    END IF;

END //

DELIMITER ;