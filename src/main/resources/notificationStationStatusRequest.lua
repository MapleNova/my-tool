HzGov_NotificationStationStatus = {}

function HzGov_NotificationStationStatus.notification_stationStatus(jsonStr)
    local data, err = json.decode(jsonStr)
    if err then
        return nil, err
    end

    local status = 1 -- 默认值为1
    if data.connectorStatus == 0 then
        status = 0
    elseif data.connectorStatus == 2 then
        if data.chargingState == 0 then
            status = 2
        elseif data.chargingState == 1 then
            status = 2
        else
            status = 3
        end
    elseif data.connectorStatus == 3 then
        status = 4
    elseif data.connectorStatus == 4 then
        -- 因255需要填写FaultType，所以按离线处理
        status = 0
    end

    local convertedData = {
        ConnectorStatusInfo = {
            ConnectorID = data.connectorTpid,
            Status = status,
            ParkStatus = 0,
            LockStatus = 0
        }

    }

   return json.encode(convertedData)

end
