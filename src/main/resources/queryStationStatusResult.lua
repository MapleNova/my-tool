queryStationStatusResultNingboGov = {}

function queryStationStatusResultNingboGov.query_station_status(jsonStr)
    local jsonData, err = json.decode(jsonStr)
    if err then
        return nil, err
    end
    local convertedData = {
        StationStatusInfos = {}
    }

    for _, stationStatus in ipairs(jsonData) do
        local stationStatusInfo = {
            StationID = stationStatus.stationId,
            ConnectorStatusInfos = {}
        }
        for _, connectorStatus in ipairs(stationStatus.connectorStatusInfos) do
            if connectorStatus.connectorTpid ~= nil and connectorStatus.connectorTpid ~= "" then
                local connectorStatusInfo = {
                    ConnectorID = connectorStatus.connectorTpid,
                    Status = connectorStatus.status,
                    ParkStatus = 0,
                    LockStatus = 0
                }
                table.insert(stationStatusInfo.ConnectorStatusInfos, connectorStatusInfo)
            end
        end
        table.insert(convertedData.StationStatusInfos, stationStatusInfo)
    end

    return json.encode(convertedData)

end
