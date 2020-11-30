// npm install --save neo4j-driver
// node example.js
const neo4j = require('neo4j-driver');
const driver = neo4j.driver('neo4j+s://demo.neo4jlabs.com:7687',
                  neo4j.auth.basic('northwind', 'northwind'), 
                  {/* encrypted: 'ENCRYPTION_OFF' */});

const query =
  `
  MATCH (p:Product)-[:PART_OF]->(:Category)-[:PARENT*0..]->
  (:Category {categoryName:$category})
  RETURN p.productName as product
  `;

const params = {"category": "Dairy Products"};

const session = driver.session({database:"northwind"});

session.run(query, params)
  .then((result) => {
    result.records.forEach((record) => {
        console.log(record.get('product'));
    });
    session.close();
    driver.close();
  })
  .catch((error) => {
    console.error(error);
  });
