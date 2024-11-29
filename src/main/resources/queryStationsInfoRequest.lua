queryStationsInfoRequestNingboGov = {}

function queryStationsInfoRequestNingboGov.query_stations_info(jsonStr)
    local jsonData, err = json.decode(jsonStr)
    if err then
        return nil, err
    end
    local convertedData = {
        LastQueryTime = jsonData.LastQueryTime,
        PageNo = jsonData.PageNo,
        PageSize = jsonData.PageSize
    }
    return json.encode(convertedData)
end
