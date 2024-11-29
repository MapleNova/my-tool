queryStationStatusRequestNingboGov = {}

function queryStationStatusRequestNingboGov.query_station_status(jsonStr)
    local jsonData, err = json.decode(jsonStr)
    if err then
        return nil, err
    end
    local convertedData = {
        StationIDs = jsonData.StationIDs,
    }
    return json.encode(convertedData)
end
