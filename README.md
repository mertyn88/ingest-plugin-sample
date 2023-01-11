# Ingest pipeline
Ingest pipeline을 사용하면 인덱싱하기 전에 데이터를 가공할 수 있다. 예를 들어 필드를 제거하고, 텍스트에서 값을 추출하고, 데이터를 보강할 수 있다.  
Ingest pipeline processor가 실행된 후 Elasticsearch는 변환된 문서를 data stream 또는 index에 추가한다. 


## 조건
* Ingest 노드가 pipeline을 처리한다. Ingest pipeline을 사용하려면 클러스터에 수집(ingest) 역할을 하는 노드가 하나 이상 있어야 한다. 수집 로드(loads)가 많은 경우 전용 ingest node를 만드는 것이 좋다.
* Elasticsearch 보안 기능이 활성화된 경우, 클러스터 권한(cluster privillege) 중 manage_pipeline, manage_ingest_pipelines 또는 manage 권한이 있어야 한다.
* 예제의 ES버전은 8.5.2이며, 이때 빌드하는 Jdk버전은 17이상이여야 한다.

## build
IntelliJ로 작업시 다음과 같이 처리한다.
1. gradle -> Tasks 클릭
2. Tasks -> other 클릭
3. other -> buildPluginZip 더블클릭

## build zip
```bash
ingest-plugin-sample/build/distributions/sample-ingest-1.0-SNAPSHOT-plugin.zip
```

## docker-compose up
```bash
docker-compose -f ./docker-compose.yml up -d
```

## docker cp
```bash
docker cp ./sample-ingest-1.0-SNAPSHOT-plugin.zip es01:/usr/share/elasticsearch/
```

## docker exec install
```bash
./bin/elasticsearch-plugin install file:///usr/share/elasticsearch/sample-ingest-1.0-SNAPSHOT-plugin.zip
```


## kibana example 
```json
POST _ingest/pipeline/_simulate
{
  "pipeline": {
    "description": "Decompose the field by separator",
    "processors": [
      { 
        "set": { 
          "description": "Ingest된 시간을 기록",
          "field": "_source.ingest_time", 
          "value": "{{_ingest.timestamp}}" 
        } 
      },
      {
        "example": {
          "field" : "test1", 
          "event" : "A"
        }
      },
      {
        "example": {
          "field" : "test2", 
          "event" : "B"
        }
      },
      {
        "example": {
          "field" : "test3", 
          "event" : "C"
        }
      }
    ]
  },
  "docs": [
    {
      "_source": {
        "test1": "hihi",
        "test2": "hoho",
        "test3": "A,B,C,D,E,FG"
      }
    }
  ]
}
```

result
```json
{
  "docs": [
    {
      "doc": {
        "_index": "_index",
        "_id": "_id",
        "_version": "-3",
        "_source": {
          "test2": "Event B !!! hoho",
          "test3": [
            "A",
            "B",
            "C",
            "D",
            "E",
            "FG"
          ],
          "ingest_time": "2023-01-11T04:59:59.051157653Z",
          "test1": "hihi Event A!!!"
        },
        "_ingest": {
          "timestamp": "2023-01-11T04:59:59.051157653Z"
        }
      }
    }
  ]
}
```